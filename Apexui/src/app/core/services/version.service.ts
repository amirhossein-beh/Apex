import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Version } from '../../models/version.model';

@Injectable({ providedIn: 'root' })
export class VersionService {

  constructor(private http: HttpClient) {}

  getAll() {
    return this.http.get<Version[]>(`${environment.apiUrl}/versions`);
  }

  upload(formData: FormData) {
    return this.http.post<Version>(`${environment.apiUrl}/versions/upload`, formData);
  }

  activate(id: number) {
    return this.http.put<string>(`${environment.apiUrl}/versions/${id}/activate`, { responseType: 'text' });
  }
}
