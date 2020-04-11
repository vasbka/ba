Rails.application.routes.draw do  
  resources :procedures
  resources :experts do
    resources :expProcedures, controller: 'exp_procedures'
  end
  resources :time_slots
end
