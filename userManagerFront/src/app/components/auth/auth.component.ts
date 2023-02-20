import { Component, OnInit } from '@angular/core';
import {AuthServiceService} from "../../services/auth.service";

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css']
})
export class AuthComponent implements OnInit {

  email:string;
  password:string;

  constructor(private authService: AuthServiceService) {
    this.email = '';
    this.password = '';
  }

  ngOnInit(): void {
  }



}
