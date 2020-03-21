Rails.application.routes.draw do
  resources :procedures
  resources :experts do
    resources :procedure
  end
  resources :time_slots
end
