import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';
import { ContactService } from '../contact.service';

@Component({
  selector: 'app-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './form.html',
})
export class Form implements OnInit {
  fb = inject(FormBuilder);
  router = inject(Router);
  route = inject(ActivatedRoute);
  service = inject(ContactService); // Add service

  form!: FormGroup;
  editingId: number | null = null;

  ngOnInit() {
    this.form = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', Validators.required],
    });

    this.editingId = this.route.snapshot.paramMap.get('id')
      ? +this.route.snapshot.paramMap.get('id')!
      : null;
    if (this.editingId) {
      const contact = this.service.getById(this.editingId);
      if (contact) this.form.patchValue(contact);
    }
  }

  save() {
    if (this.form.valid) {
      const data = this.form.value;
      if (this.editingId) {
        this.service.update(this.editingId, data); // Fixed type
      } else {
        this.service.add(data); // Fixed type
      }
      this.router.navigate(['/contacts']);
    }
  }

  get isEdit() {
    return !!this.editingId;
  }
}
