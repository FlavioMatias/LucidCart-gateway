import { 
  ApplicationConfig, 
  provideBrowserGlobalErrorListeners, 
  provideZoneChangeDetection 
} from '@angular/core';

import { provideRouter } from '@angular/router';
import { routes } from './app.routes';

import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './shared/services/user/user-interceptor';

export const HOST = "localhost:8080";
export const BASE_URL = `http://${HOST}/api/v1`;

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),

    provideHttpClient(
      withInterceptors([authInterceptor])
    ),

    provideZoneChangeDetection({ eventCoalescing: true }),

    provideRouter(routes)
  ]
};
