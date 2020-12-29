Rails.application.routes.draw do
  # For details on the DSL available within this file, see https://guides.rubyonrails.org/routing.html

  namespace :api, defaults: { format: :json } do
    namespace :v1 do
      # sign up
      resources :registrations, path: 'sign_up', only: :create
      # sign in
      resources :sessions, path: '', only: [] do
        collection do
          post 'sign_in', to: 'sessions#create'
        end
      end
      # Password
      resources :passwords, only: [] do
        collection do
          put :forgot
          put :reset
        end
      end
      # Users
      resources :users, only: [] do
        collection do
          put :verify
          put :select_default_storage
          put :select_mode
        end
      end
      # Preference
      resources :preferences, only: [:create] do
        collection do
          get :show
          put :update
        end
      end
    end
  end
end
