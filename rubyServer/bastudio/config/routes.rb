Rails.application.routes.draw do
  resources :procedures do
    resources :experts
  end
  resources :experts do
    resources :expProcedures, controller: 'exp_procedures'
    resources :procedures, controller: 'procedures'
  end
  resources :time_slots
end
