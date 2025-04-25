import { Component } from '@angular/core';
import {AuthService} from '../../@core/services/auth/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

  isAuthenticated = false;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
       this.authService.isAuthenticated$.subscribe((authState) => {
            this.isAuthenticated = authState;
          })
        }

  logout(): void {
    this.authService.logout();
    this.isAuthenticated = false;
  }
}
