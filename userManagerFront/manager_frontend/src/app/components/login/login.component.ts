import { Component } from '@angular/core';
import {LoginService} from "../../services/login.service";
import {Router} from "@angular/router";
import * as path from "path";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  email: string;
  password: string;

  constructor(private loginService: LoginService,
              private router: Router) {
    this.email = '';
    this.password = '';
  }

  login() {
    this.loginService.login(this.email, this.password).subscribe(res => {
      if (res.status == 200) {
        console.log(res.body.jwt);
        this.loginService.setToken(res.body.jwt);
      }
      else {
        alert("Wrong username or password!")
      }
    })
  }

}
