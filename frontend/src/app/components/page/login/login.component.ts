import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../@core/services/auth.service';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import {ErrorModalComponent} from '../../error-modal/error-modal.component';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  hidePassword = true;

  constructor(private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private dialog: MatDialog) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      rememberMe: [false]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
     const credentials = this.loginForm.value;
           this.authService.login(credentials).subscribe({
             next: (response) => {
              this.router.navigate(['/portfolio']);
             },
             error: (err) => {
               this.dialog.open(ErrorModalComponent, {
                 data: {
                   title: 'Login Error',
                   text: 'Email or password is incorrect. Please try again.'
                 },
                 width: '400px',
                 disableClose: true
               });
             }
           });
    } else {
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
