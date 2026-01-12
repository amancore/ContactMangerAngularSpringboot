import { Injectable, signal } from '@angular/core';
import { Contact } from './state/contact';

@Injectable({ providedIn: 'root' })
export class ContactService {
  #contacts = signal<Contact[]>([]);

  contacts = this.#contacts.asReadonly();

  constructor() {
    const saved = localStorage.getItem('contacts');
    this.#contacts.set(
      saved
        ? (JSON.parse(saved) as Contact[])
        : [
            { id: 1, name: 'John Doe', email: 'john@test.com', phone: '123-456-7890' },
            { id: 2, name: 'Jane Smith', email: 'jane@test.com', phone: '098-765-4321' },
          ]
    );
  }

  add(contactData: { name: string; email: string; phone: string }) {
    const newId = Math.max(0, ...this.#contacts().map((c) => c.id)) + 1;
    const newContact: Contact = { id: newId, ...contactData };
    this.#contacts.update((c) => [newContact, ...c]);
    this.save();
  }

  update(id: number, contactData: Partial<Contact>) {
    this.#contacts.update((c) => c.map((c) => (c.id === id ? { ...c, ...contactData } : c)));
    this.save();
  }

  delete(id: number) {
    this.#contacts.update((c) => c.filter((c) => c.id !== id));
    this.save();
  }

  getById(id: number): Contact | undefined {
    return this.#contacts().find((c) => c.id === id);
  }

  private save() {
    localStorage.setItem('contacts', JSON.stringify(this.#contacts()));
  }
}
