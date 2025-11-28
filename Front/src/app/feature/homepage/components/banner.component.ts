import { Component} from "@angular/core";
import { CommonModule } from "@angular/common";

@Component({
  selector: 'app-home-banner',
  standalone: true,
  imports: [CommonModule],
  template: ` 
    <div class="bg-[#fff] w-[100vw] h-[90vh] relative border-b-[1px] border-gray-300">

        <h2 class="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 text-center">
            <span class="text-[1.5rem]">Wellcome to</span><br>
            <span class="text-[5vw] font-medium text-stroke">
            Lucid 
            <span class="font-bold text-blue-600">Cart</span>
            </span>
        </h2>

        <img
            src="assets/ec-easy-shopping.png"
            alt=""
            class="absolute bottom-[-5vh] left-10 w-[20vw] pointer-events-none select-none"
        />
        <img src="assets/ec-marketing.png" alt=""
              class="absolute bottom-[0] right-10 w-[20vw] pointer-events-none select-none">

    </div>
  `,
})
export class BannerComponent {

}