import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IPost } from 'app/entities/post/post.model';
import { PostService } from 'app/entities/post/service/post.service';
import { LikeService } from '../service/like.service';
import { ILike } from '../like.model';
import { LikeFormService } from './like-form.service';

import { LikeUpdateComponent } from './like-update.component';

describe('Like Management Update Component', () => {
  let comp: LikeUpdateComponent;
  let fixture: ComponentFixture<LikeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let likeFormService: LikeFormService;
  let likeService: LikeService;
  let postService: PostService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), LikeUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(LikeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LikeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    likeFormService = TestBed.inject(LikeFormService);
    likeService = TestBed.inject(LikeService);
    postService = TestBed.inject(PostService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Post query and add missing value', () => {
      const like: ILike = { id: 456 };
      const post: IPost = { id: 13453 };
      like.post = post;

      const postCollection: IPost[] = [{ id: 14239 }];
      jest.spyOn(postService, 'query').mockReturnValue(of(new HttpResponse({ body: postCollection })));
      const additionalPosts = [post];
      const expectedCollection: IPost[] = [...additionalPosts, ...postCollection];
      jest.spyOn(postService, 'addPostToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ like });
      comp.ngOnInit();

      expect(postService.query).toHaveBeenCalled();
      expect(postService.addPostToCollectionIfMissing).toHaveBeenCalledWith(
        postCollection,
        ...additionalPosts.map(expect.objectContaining),
      );
      expect(comp.postsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const like: ILike = { id: 456 };
      const post: IPost = { id: 15018 };
      like.post = post;

      activatedRoute.data = of({ like });
      comp.ngOnInit();

      expect(comp.postsSharedCollection).toContain(post);
      expect(comp.like).toEqual(like);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILike>>();
      const like = { id: 123 };
      jest.spyOn(likeFormService, 'getLike').mockReturnValue(like);
      jest.spyOn(likeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ like });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: like }));
      saveSubject.complete();

      // THEN
      expect(likeFormService.getLike).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(likeService.update).toHaveBeenCalledWith(expect.objectContaining(like));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILike>>();
      const like = { id: 123 };
      jest.spyOn(likeFormService, 'getLike').mockReturnValue({ id: null });
      jest.spyOn(likeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ like: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: like }));
      saveSubject.complete();

      // THEN
      expect(likeFormService.getLike).toHaveBeenCalled();
      expect(likeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILike>>();
      const like = { id: 123 };
      jest.spyOn(likeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ like });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(likeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePost', () => {
      it('Should forward to postService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(postService, 'comparePost');
        comp.comparePost(entity, entity2);
        expect(postService.comparePost).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
