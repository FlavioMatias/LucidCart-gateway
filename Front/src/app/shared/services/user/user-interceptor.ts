import { HttpEvent, HttpHandlerFn, HttpRequest } from "@angular/common/http";
import { inject } from "@angular/core";
import { Router } from "@angular/router";
import { Observable, throwError } from "rxjs";
import { AuthService } from "./user-service";

export function authInterceptor(
  req: HttpRequest<unknown>,
  next: HttpHandlerFn
): Observable<HttpEvent<unknown>> {

  // Public routes
  if (req.url.includes('/signin') || req.url.includes('/signup')) {
    return next(req);
  }

  const userService = inject(AuthService);
  const router = inject(Router);

  if (userService.isTokenExpired()) {
    router.navigate(['/login']);
    return throwError(() => new Error('JWT expired'));
  }

  const token = sessionStorage.getItem('token');

  if (token) {
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(authReq);
  }

  return next(req);
}
