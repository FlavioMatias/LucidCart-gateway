import { Component, Input } from "@angular/core";
import { CommonModule } from "@angular/common";
import { CardComponent } from "../../../shared/components/card.component";
import { ButtonComponent } from "../../../shared/components/button.component";

@Component({
  selector: 'app-pets-category-card',
  standalone: true,
  imports: [CommonModule, CardComponent, ButtonComponent],
  template: `
    <div class="">
      <app-card [width]="'45vw'" [height]="'38vh'">
        <div class="flex justify-between items-center px-6">

          <!-- Esquerda -->
          <img
            src="assets/pets.png"
            alt="Pets"
            class="w-[20rem] h-auto"
          />

          <!-- Direita -->
          <div class="flex flex-col items-center mr-[6rem]">

            <span class="text-gray-700 text-xl">Itens for yours</span>

            <div class="flex items-center gap-3 mb-8">
              <img src="assets/foot.svg" alt="" class="w-[3rem] h-auto">
              <h3 class="text-[3.5rem] font-bold text-blue-600">Pets</h3>
            </div>

            <app-button
              text="See"
              icon="{{ icon }}"
              [routerLink]="['/pets']"
              class="mt-6"
            ></app-button>

          </div>

        </div>
      </app-card>
    </div>
  `,
})
export class PetsCategoryCardComponent {
  icon: string = '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-eye-icon lucide-eye"><path d="M2.062 12.348a1 1 0 0 1 0-.696 10.75 10.75 0 0 1 19.876 0 1 1 0 0 1 0 .696 10.75 10.75 0 0 1-19.876 0"/><circle cx="12" cy="12" r="3"/></svg>';
}