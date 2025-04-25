import { Injectable } from '@angular/core';
import {endpoint} from '../../../environments';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators'; // Import tap
import { Router } from '@angular/router';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
   private url = endpoint(`/api/auth`);

   constructor(private http: HttpClient, private router: Router) {}

  register(user: any): Observable<any> {
    return this.http.post(`${this.url}/register`, user);
  }

  login(credentials: { email: string; password: string }): Observable<any> {
    return this.http.post<{ token: string }>(`${this.url}/login`, credentials).pipe(
      tap(response => {
        localStorage.setItem('token', response.token);
      })
    );
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
      return !!localStorage.getItem('token');
    }

}
