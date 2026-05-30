import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const token = localStorage.getItem('token');

  // اگه token داشتیم به هر request اضافه میکنه
  const authReq = token ? req.clone({ headers: req.headers.set('Authorization', `Bearer ${token}`) }) : req;

  return next(authReq).pipe(

    catchError(error => {
      const isLoginRequest = req.url.includes('/auth/login');
      if (error.status === 401 && !isLoginRequest) {
        localStorage.removeItem('token');
        router.navigate(['/login']);
      }
      return throwError(() => error);
    })
  );
};
