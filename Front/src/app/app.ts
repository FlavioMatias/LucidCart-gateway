import { Component } from '@angular/core';
import { SearchResultsComponent } from './feature/products/products-src';
import { Header } from './layout/header.component';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [SearchResultsComponent, Header, RouterOutlet],
  template: `
    <div class="bg-[#F7F7F7] overflow-x-hidden">

      <app-header
        (searchActive)="handleSearchActive($event)"
        (searchResults)="updateResults($event)"
        (searchQuery)="updateQuery($event)"
      ></app-header>

      @if (searchMode) {
        <app-search-results 
          [products]="products"
          [lastQuery]="query">
        </app-search-results>
      } @else {
        <router-outlet></router-outlet>
      }

    </div>
  `
})
export class App {
  searchMode = false;
  products: any[] = [];
  query = "";
  title = 'LucidCart';

  handleSearchActive(active: boolean) {
    this.searchMode = active;
  }

  updateResults(list: any[]) {
    this.products = list;
  }

  updateQuery(q: string) {
    this.query = q;
  }
}
