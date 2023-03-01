import { Component } from '@angular/core';
import {ErrorMessageServiceService} from "../../services/error-message-service.service";

@Component({
  selector: 'app-error-message-component',
  templateUrl: './error-message-component.component.html',
  styleUrls: ['./error-message-component.component.css']
})
export class ErrorMessageComponentComponent {

  errors: any[];

  constructor(private errorMessageService: ErrorMessageServiceService) {
    this.errors = [];
  }

  getErrors() {
    this.errorMessageService.getErrors().subscribe(res => {
      if (res.status == 200) {
        this.errors = res.body;
      } else{
        alert("Error happend");
      }
    })
  }

}
