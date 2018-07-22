import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {LinkToComponent} from './link-to.component';

describe('LinkToComponent', () => {
  let component: LinkToComponent;
  let fixture: ComponentFixture<LinkToComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [LinkToComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LinkToComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
