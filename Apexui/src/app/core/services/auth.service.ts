import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { LoginRequest, LoginResponse } from '../../models/auth.model';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private readonly TOKEN_KEY = 'token';
  private readonly USERNAME_KEY = 'username';
  private readonly ROLE_KEY = 'role';

  // signal برای وضعیت لاگین - کامپوننت‌ها میتونن بهش گوش بدن
  isLoggedIn = signal<boolean>(this.hasToken());

  constructor(private http: HttpClient, private router: Router) {}

  login(request: LoginRequest) {
    return this.http.post<LoginResponse>(`${environment.apiUrl}/auth/login`, request).pipe(
      tap(response => {
        localStorage.setItem(this.TOKEN_KEY, response.token);
        localStorage.setItem(this.USERNAME_KEY, response.username);
        localStorage.setItem(this.ROLE_KEY, response.role);
        this.isLoggedIn.set(true);
      })
    );
  }

  logout() {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USERNAME_KEY);
    localStorage.removeItem(this.ROLE_KEY);
    this.isLoggedIn.set(false);
    this.router.navigate(['/login']);
  }

  getUsername(): string {
    return localStorage.getItem(this.USERNAME_KEY) ?? '';
  }

  getRole(): string {
    return localStorage.getItem(this.ROLE_KEY) ?? '';
  }

  private hasToken(): boolean {
    return !!localStorage.getItem(this.TOKEN_KEY);
  }
}
