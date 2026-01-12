import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ContactService } from '../contact.service';

@Component({
  selector: 'app-list',
  imports: [RouterLink],
  templateUrl: './list.html',
})
export class List {
  service = inject(ContactService);
  contacts = this.service.contacts;

  delete(id: number | undefined) {
    if (id !== undefined) {
      this.service.delete(id);
    }
  }
}
