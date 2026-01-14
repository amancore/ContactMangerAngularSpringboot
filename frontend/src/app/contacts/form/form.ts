import { Component, inject, OnInit, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';
import { ContactService } from '../contact.service';

@Component({
  selector: 'app-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './form.html',
})
export class Form implements OnInit {
  fb = inject(FormBuilder);
  router = inject(Router);
  route = inject(ActivatedRoute);
  service = inject(ContactService);

  form!: FormGroup;
  editingId: number | null = null;

  constructor() {
    effect(() => {
      const contacts = this.service.contacts();
      if (this.editingId && contacts.length) {
        const contact = contacts.find((c) => c.id === this.editingId);
        if (contact) this.form?.patchValue(contact);
      }
    });
  }

  ngOnInit() {
    this.form = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', Validators.required],
    });

    const idStr = this.route.snapshot.paramMap.get('id');
    this.editingId = idStr ? Number(idStr) : null;
  }

  save() {
    if (this.form.valid) {
      const data = this.form.value as { name: string; email: string; phone: string };
      if (this.editingId) {
        this.service.update(this.editingId, data);
      } else {
        this.service.add(data);
      }
      this.router.navigate(['/contacts']);
    }
  }

  get isEdit() {
    return !!this.editingId;
  }
}
