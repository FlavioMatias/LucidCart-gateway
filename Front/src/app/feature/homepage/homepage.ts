import { Component, OnInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { BannerComponent } from "./components/banner.component";
import { PetsCategoryCardComponent } from "./components/pets-category-card.component";
import { TechCategoryCardComponent } from "./components/tech-category-card.component";
import { ProductCardComponent } from "./components/product-card.component";
import { ProductService } from "../../shared/services/products/products-service";
import { ProductSimple } from "../../shared/models/product/product-simple.model";
import { ProductsOptionsComponent } from "./components/products-options.component";

@Component({
  selector: 'app-homepage',
  standalone: true,
  imports: [
    CommonModule, 
    BannerComponent, 
    PetsCategoryCardComponent, 
    TechCategoryCardComponent,
    ProductCardComponent,
    ProductsOptionsComponent,
  ],
  template: `
<app-home-banner></app-home-banner>

<div class="my-14 flex justify-center gap-24">
  <app-tech-category-card></app-tech-category-card>
  <app-pets-category-card></app-pets-category-card>
</div>

<!-- CONTAINER GERAL -->
<div class="flex gap-10 px-10 mt-10">

  <!-- MENU LATERAL 
  <div class="w-[20vw] min-w-[220px] pr-6">
    <app-products-options
    [sections]="menuData"
    (optionSelected)="filtrar($event)"
    ></app-products-options>
  </div>
  -->

  <!-- LISTA DE PRODUTOS -->
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
  products: ProductSimple[] = [];
  menuData = [
  {
    label: 'All Product',
    icon:'',
    open: true,
    children: [
      { label: 'Categoria A' },
      { label: 'Categoria B' },
      { label: 'Categoria C' },
      { label: 'Categoria D' }
    ]
  },
  {
    label: 'new Arrival',
    open: false,
    children: []
  },
  {
    label: 'on discount',
    open: false,
    children: []
  }
];


  constructor(private productService: ProductService) {}

  ngOnInit() {
    this.products = this.productService.getAll();
  }
  filtrar(categoria: string) {
    console.log(categoria);
  }
}
