import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';
import { AuthGuard } from './auth.guard';
import { RouterTestingModule } from '@angular/router/testing';

describe('AuthGuard', () => {
  let authService: jasmine.SpyObj<AuthService>;
  let router: Router;
  let guard: AuthGuard;

  beforeEach(() => {
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['isAuthenticated']);
    
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      providers: [
        AuthGuard,
        { provide: AuthService, useValue: authServiceSpy },
      ],
    });

    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    router = TestBed.inject(Router);
    guard = TestBed.inject(AuthGuard);
  });

  it('should allow activation if authenticated', () => {
    authService.isAuthenticated.and.returnValue(true);
    const result = guard.canActivate();
    expect(result).toBeTrue();
  });

  it('should redirect to login if not authenticated', () => {
    authService.isAuthenticated.and.returnValue(false); 

    spyOn(router, 'navigate');

    const result = guard.canActivate();
    expect(result).toBeFalse();
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });
});
