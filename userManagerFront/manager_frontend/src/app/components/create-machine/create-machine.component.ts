import { Component } from '@angular/core';
import {CreateMachineService} from "../../services/create-machine.service";

@Component({
  selector: 'app-create-machine',
  templateUrl: './create-machine.component.html',
  styleUrls: ['./create-machine.component.css']
})
export class CreateMachineComponent {

  active: boolean;

  name: string;

  constructor(private createMachineService: CreateMachineService) {
    this.active = false;
    this.name = '';
  }

  newMachine() {
    this.createMachineService.newMachine(this.active, this.name).subscribe(res => {
      if (res.status == 200) {
        alert("Machine added successfully!");
      }
      else {
        alert("Not authorized");
      }
    })
  }

}
