import { Injectable } from '@angular/core';
import { Match } from '../core/models/match';

@Injectable({
  providedIn: 'root'
})
export class StrategyService {

  constructor() { }

  getCommands(match: Match): any[] {
    const commands = [];

    // TODO

    return commands;
  }
}
