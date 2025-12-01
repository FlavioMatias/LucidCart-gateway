import { Component, Input } from '@angular/core';
import { ProductDTO } from '../../shared/services/products/products-service';
import { ProductCardComponent } from '../homepage/components/product-card.component';

@Component({
  selector: 'app-search-results',
  standalone: true,
  imports: [ProductCardComponent],
  template: `
    <div class="pt-32 px-10">

      @if (products.length > 0) {
        <h2 class="text-2xl font-semibold mb-6">
          Results for "{{ lastQuery }}"
        </h2>
      }

      @if (products.length > 0) {
        <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">

          @for (p of products; track p.id) {
            <app-product-card [product]="p" />
          }

        </div>
      } @else {
        <div class="text-center text-gray-500 text-xl">
          No products found.
        </div>
      }

    </div>
  `
})
export class SearchResultsComponent {
  @Input() products: ProductDTO[] = [];
  @Input() lastQuery: string = "";
}
