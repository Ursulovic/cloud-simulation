import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {UserPermission} from "../../model";

@Component({
  selector: 'app-new-user',
  templateUrl: './new-user.component.html',
  styleUrls: ['./new-user.component.css']
})
export class NewUserComponent {

  name: string;
  surname: string;
  password: string;
  email: string;
  permissions: UserPermission[];
  constructor(private router: Router) {
    this.name = '';
    this.surname = '';
    this.password = '';
    this.email = '';
    this.permissions = [];
  }

  addNewUser() {

  }


}
