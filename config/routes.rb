Rails.application.routes.draw do

  # For details on the DSL available within this file, see https://guides.rubyonrails.org/routing.html

  namespace :api, defaults: { format: :json } do
    namespace :v1 do
      # sign up
      resources :registrations, path: 'sign_up', only: :create
      # sign in
      resources :sessions, path: '', only: [] do
        collection do
          post  'sign_in',  to: 'sessions#create'
          put   'sign_out', to: 'sessions#update'
        end
      end
      # Password
      resources :passwords, only: [] do
        collection do
          put :forgot
          put :reset
          put :change
        end
      end
      # Users
      resources :users, only: [] do
        collection do
          get     'profile', to: 'users#show'
          put     :update
          put     :verify
          put     :select_default_storage
          put     :select_mode
          delete  :destroy
        end
      end
      # Preference
      resources :preferences, only: [:create] do
        collection do
          get :show
          put :update
        end
      end
      # Permission
      resources :permissions, only: [:create] do
        collection do
          get :show
          put :update
        end
      end
      # Feedback
      resources :feedbacks, only: [:create]
      # FreeAd
      resources :free_ads, only: [:index]
      # Subscription
      resources :subscriptions, only: [:create]
      # Pages
      resources :pages, only: [] do
        collection do
          get :about_us
          get :privacy_policy
          get :terms_conditions
          get :legal_disclaimer
        end
      end
      # User Videos
      resources :videos, only: [:index, :create]
      # Notifications
      resources :notifications, only: [:index]
      # Post Feeds
      resources :post_feeds, only: [:create]
    end
  end

  root to: 'admin/users#index'

  devise_for :admins, path: 'admin', only: :sessions

  namespace :admin do
    get 'feeds/index'
    get 'feeds/show'
    get 'feeds/destoy'
    resources :users
    resources :feedbacks, only: [:index, :show, :destroy]
    resources :post_feeds
    resources :email_templates
    resources :pages, only: [:create, :update, :destroy] do
      collection do
        get :about_us
        get :privacy_policy
        get :terms_conditions
        get :legal_disclaimer
      end
    end
    resources :free_ads, only: [:index]
  end

  resources :passwords, path: '', only: [] do
    collection do
      get :app_forgot_password
    end
  end
end
