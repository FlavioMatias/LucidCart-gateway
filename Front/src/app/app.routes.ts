import { Routes } from '@angular/router';
import { HomePage } from './feature/homepage/homepage';
import { CartPageComponent } from './feature/cart/cart-page';
import { OrdersPageComponent } from './feature/orders/orders-page';
import { LoginPageComponent } from './feature/auth/login-page';
import { SignupPageComponent } from './feature/auth/signup-page';

export const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: HomePage },
  { path: 'cart', component: CartPageComponent },
  { path: 'orders', component: OrdersPageComponent },
  { path: 'login', component: LoginPageComponent },
  { path: 'signup', component: SignupPageComponent },
]