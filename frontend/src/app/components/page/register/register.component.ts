import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../@core/services/auth.service';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { ErrorModalComponent } from '../../error-modal/error-modal.component';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  registerForm: FormGroup;
  hidePassword = true;

  constructor(private fb: FormBuilder,
    private authService: AuthService,
    private dialog: MatDialog,
    private router: Router) {
    this.registerForm = this.fb.group({
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]]
    });
  }

  onSubmit() {
    if (this.registerForm.valid) {
     const user = this.registerForm.value;
           this.authService.register(user).subscribe({
             next: () => {
                this.router.navigate(['/login']);
             },
             error: (err) => {
               console.log(err)
                this.dialog.open(ErrorModalComponent, {
                  data: {
                    title: 'Registration Error',
                    text: err.error || 'An error occurred during registration. Please try again.'
                  },
                  width: '400px',
                  disableClose: true
                });
              }
           });
    } else {
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
