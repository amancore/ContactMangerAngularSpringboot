import { Injectable, signal } from '@angular/core';
import { Contact } from './state/contact';
import { HttpClient } from '@angular/common/http';
import { tap, catchError, of } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ContactService {
  private apiUrl = '/api/contacts';
  #contacts = signal<Contact[]>([]);

  contacts = this.#contacts.asReadonly();

  constructor(private http: HttpClient) {
    this.http
      .get<Contact[]>(this.apiUrl)
      .pipe(
        tap((contacts) => {
          this.#contacts.set(contacts);
          localStorage.setItem('contacts', JSON.stringify(contacts));
        }),
        catchError(() => {
          this.loadLocal();
          return of([]);
        })
      )
      .subscribe();
  }

  loadLocal() {
    const saved = localStorage.getItem('contacts');
    this.#contacts.set(saved ? (JSON.parse(saved) as Contact[]) : []);
  }
  add(data: { name: string; email: string; phone: string }) {
    console.log('🚀 POST data:', data); // DEBUG 1

    this.http
      .post<Contact>(this.apiUrl, data)
      .pipe(
        tap((contact) => {
          console.log('✅ Created:', contact); // DEBUG 2
          this.#contacts.update((c) => [...c, contact]);
          localStorage.setItem('contacts', JSON.stringify(this.#contacts()));
        }),
        catchError((err) => {
          console.error('❌ POST Error:', err); // DEBUG 3
          return of(null);
        })
      )
      .subscribe();
  }

  update(id: number, contactData: Partial<Contact>) {
    const contact = this.#contacts().find((c) => c.id === id);
    if (contact) {
      this.http
        .put<Contact>(`${this.apiUrl}/${id}`, { ...contact, ...contactData })
        .pipe(
          tap((updated) => {
            this.#contacts.update((c) => c.map((c) => (c.id === id ? updated : c)));
            localStorage.setItem('contacts', JSON.stringify(this.#contacts()));
          })
        )
        .subscribe();
    }
  }

  delete(id: number) {
    this.http
      .delete(`${this.apiUrl}/${id}`)
      .pipe(
        tap(() => {
          this.#contacts.update((c) => c.filter((c) => c.id !== id));
          localStorage.setItem('contacts', JSON.stringify(this.#contacts()));
        })
      )
      .subscribe();
  }

  getById(id: number): Contact | undefined {
    return this.#contacts().find((c) => c.id === id);
  }

  //
  //     const saved = localStorage.getItem('contacts');
  //     this.#contacts.set(
  //       saved
  //         ? (JSON.parse(saved) as Contact[])
  //         : [
  //             { id: 1, name: 'John Doe', email: 'john@test.com', phone: '123-456-7890' },
  //             { id: 2, name: 'Jane Smith', email: 'jane@test.com', phone: '098-765-4321' },
  //           ]
  //     );
  // }

  // add(contactData: { name: string; email: string; phone: string }) {
  //   const newId = Math.max(0, ...this.#contacts().map((c) => c.id)) + 1;
  //   const newContact: Contact = { id: newId, ...contactData };
  //   this.#contacts.update((c) => [newContact, ...c]);
  //   this.save();
  // }

  //   update(id: number, contactData: Partial<Contact>) {
  //     this.#contacts.update((c) => c.map((c) => (c.id === id ? { ...c, ...contactData } : c)));
  //     this.save();
  //   }
  //
  //   delete(id: number) {
  //     this.#contacts.update((c) => c.filter((c) => c.id !== id));
  //     this.save();
  //   }
  //
  //   getById(id: number): Contact | undefined {
  //     return this.#contacts().find((c) => c.id === id);
  //   }
  //
  private save() {
    localStorage.setItem('contacts', JSON.stringify(this.#contacts()));
  }
}
