import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export interface User {
  id: string;
  username: string;
  role: string;
  active: boolean;
  deletable: boolean;
  createdAt: string;
}

@Injectable({ providedIn: 'root' })
export class UserService {

  constructor(private http: HttpClient) {}

  getAll() {
    return this.http.get<User[]>(`${environment.apiUrl}/users`);
  }

  create(data: { username: string; password: string; role: string }) {
    return this.http.post<User>(`${environment.apiUrl}/users`, data);
  }

  changePassword(id: string, newPassword: string) {
    return this.http.put<User>(`${environment.apiUrl}/users/${id}/password`, { newPassword });
  }

  toggleActive(id: string) {
    return this.http.put<User>(`${environment.apiUrl}/users/${id}/toggle-active`, {});
  }

  delete(id: string) {
    return this.http.delete(`${environment.apiUrl}/users/${id}`, { responseType: 'text' });
  }
}
