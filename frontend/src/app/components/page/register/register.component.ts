import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  registerForm: FormGroup;
  hidePassword = true;

  constructor(private fb: FormBuilder) {
    this.registerForm = this.fb.group({
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]]
    });
  }

  onSubmit() {
    if (this.registerForm.valid) {
      console.log('Registration form submitted:', this.registerForm.value);
      // todo
    } else {
      // todo
      Object.keys(this.registerForm.controls).forEach(field => {
        const control = this.registerForm.get(field);
        control?.markAsTouched({ onlySelf: true });
      });
    }
  }

  getFirstNameErrorMessage() {
    const firstName = this.registerForm.get('firstName');
    return firstName?.hasError('required') ? 'First name is required' : '';
  }

  getLastNameErrorMessage() {
    const lastName = this.registerForm.get('lastName');
    return lastName?.hasError('required') ? 'Last name is required' : '';
  }

  getEmailErrorMessage() {
    const email = this.registerForm.get('email');
    if (email?.hasError('required')) {
      return 'Email is required';
    }
    return email?.hasError('email') ? 'Please enter a valid email address' : '';
  }

  getPasswordErrorMessage() {
    const password = this.registerForm.get('password');
    if (password?.hasError('required')) {
      return 'Password is required';
    }
    return password?.hasError('minlength') ? 'Password must be at least 8 characters' : '';
  }

}
