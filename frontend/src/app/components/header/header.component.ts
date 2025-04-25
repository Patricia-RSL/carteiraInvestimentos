import { Component } from '@angular/core';
import {AuthService} from '../../@core/services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

  isAuthenticated = false;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
      this.isAuthenticated = this.authService.isAuthenticated();
    }

  logout(): void {
    this.authService.logout();
    this.isAuthenticated = false;
  }
}
