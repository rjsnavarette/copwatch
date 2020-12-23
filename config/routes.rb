Rails.application.routes.draw do
  # For details on the DSL available within this file, see https://guides.rubyonrails.org/routing.html

  namespace :api, defaults: { format: :json } do
    namespace :v1 do
      # sign up
      resources :registrations, path: 'sign_up', only: :create
      # sign in
      resources :sessions, path: '' do
        collection do
          post 'sign_in', to: 'sessions#create'
        end
      end
    end
  end
end
