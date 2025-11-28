import { Component, EventEmitter, Input, Output } from '@angular/core';

export interface MenuChild {
  label: string;
}

export interface MenuSection {
  label: string;
  open?: boolean;
  children?: MenuChild[];
}

@Component({
  selector: 'app-products-options',
  standalone: true,
  template: `
<div class="w-64 flex flex-col gap-4 text-sm text-gray-800">

  @for (section of sections; track section) {

    <!-- ====================== HEADER ====================== -->
    <button
      (click)="toggle(section)"
      class="w-full flex items-center justify-between px-3 py-2 rounded-lg
             bg-gray-50 hover:bg-gray-100 transition-colors duration-200 select-none"
      type="button"
      aria-expanded="{{ section.open ? 'true' : 'false' }}"
    >
      <div class="flex items-center gap-3">

        <!-- Ícone caixa SVG -->
        <svg class="w-5 h-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6">
          <path d="M3 7.5l9-4 9 4v8.5l-9 4-9-4V7.5z" />
          <path d="M12 3.5v8.5" />
        </svg>

        <span class="font-medium">{{ section.label }}</span>
      </div>

      <!-- seta animada -->
      <svg
        class="w-4 h-4 transition-transform duration-200"
        [class.rotate-180]="section.open"
        viewBox="0 0 24 24"
        fill="none"
        stroke="currentColor"
        stroke-width="2"
      >
        <path d="M6 9l6 6 6-6"/>
      </svg>
    </button>


    <!-- ====================== BODY ====================== -->
    <div
      class="ml-6 overflow-hidden transition-[max-height,opacity] duration-300 ease-in-out"
      [style.maxHeight.px]="section.open ? (section.children?.length ?? 0) * 32 + 12 : 0"
      [style.opacity]="section.open ? '1' : '0'"
    >

      @if ((section.children?.length ?? 0) > 0) {

        <!-- LISTA DE SUBITENS -->
        <div class="flex flex-col gap-1.5 py-2">

          @for (child of section.children; track child) {

            <button
              type="button"
              (click)="select(child)"
              class="flex items-center gap-2 px-1 py-1 text-gray-700 hover:text-gray-900 
                     transition-colors duration-150 rounded text-left"
            >
              <!-- Pontinho -->
              <div class="w-2 h-2 rounded-full bg-gray-600"></div>

              {{ child.label }}
            </button>

          }

        </div>

      }

    </div>

  }

</div>
  `
})
export class ProductsOptionsComponent {

  @Input() sections: MenuSection[] = [];
  @Output() optionSelected = new EventEmitter<string>();

  toggle(section: MenuSection) {
    section.open = !section.open;
  }

  select(child: MenuChild) {
    this.optionSelected.emit(child.label);
  }
}
