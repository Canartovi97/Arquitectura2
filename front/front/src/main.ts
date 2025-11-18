import 'zone.js';
import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
/* import { provideAnimations } from '@angular/platform-browser/animations'; */
import { appConfig } from './app/app.config';
import { App } from './app/app';

import { routes } from './app/app.routes';
import { authInterceptor } from './app/interceptors/auth.interceptor';


bootstrapApplication(App, {
  ...appConfig,
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptors([authInterceptor])),
    /* provideAnimations(), */
  ],
})
  .catch((err) => console.error(err));
