import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  hidePassword = true;

  constructor(private fb: FormBuilder) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      rememberMe: [false]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      console.log('Form submitted:', this.loginForm.value);
      //todo
    } else {
      // todo
      Object.keys(this.loginForm.controls).forEach(field => {
        const control = this.loginForm.get(field);
        control?.markAsTouched({ onlySelf: true });
      });
    }
  }

  getEmailErrorMessage() {
    const email = this.loginForm.get('email');
    if (email?.hasError('required')) {
      return 'You must enter an email';
    }
    return email?.hasError('email') ? 'Not a valid email' : '';
  }

  getPasswordErrorMessage() {
    const password = this.loginForm.get('password');
    if (password?.hasError('required')) {
      return 'You must enter a password';
    }
    return password?.hasError('minlength') ? 'Password must be at least 8 characters' : '';
  }
}
