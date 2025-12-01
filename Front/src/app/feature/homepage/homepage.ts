import { Component, OnInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { BannerComponent } from "./components/banner.component";
import { PetsCategoryCardComponent } from "./components/pets-category-card.component";
import { TechCategoryCardComponent } from "./components/tech-category-card.component";
import { ProductCardComponent } from "./components/product-card.component";
import { ProductService } from "../../shared/services/products/products-service";
import { ProductDTO } from "../../shared/services/products/products-service";

@Component({
  selector: 'app-homepage',
  standalone: true,
  imports: [
    CommonModule, 
    BannerComponent, 
    PetsCategoryCardComponent, 
    TechCategoryCardComponent,
    ProductCardComponent,
  ],
  template: `
<app-home-banner></app-home-banner>

<div class="my-14 flex justify-center gap-24">
  <app-tech-category-card></app-tech-category-card>
  <app-pets-category-card></app-pets-category-card>
</div>

<div class="flex gap-10 px-10 mt-10">

  <div class="flex-1 ml-10">
    <div
      class="
        grid 
        gap-14
        grid-cols-2
        md:grid-cols-3
        justify-items-center
      "
    >
      <app-product-card
        *ngFor="let product of products"
        [product]="product"
      ></app-product-card>
    </div>
  </div>
</div>
  `
})
export class HomePage implements OnInit {
  products: ProductDTO[] = [];

  constructor(private productsService: ProductService) {}

  ngOnInit() {
    this.productsService.listProducts().subscribe({
      next: (data) => {
        this.products = data; // data já é ProductDTO[]
      },
      error: (err) => console.error("Erro ao carregar produtos:", err)
    });
  }

  filtrar(categoria: string) {
    console.log(categoria);
  }
}
