Rails.application.routes.draw do
  get '/experts/get_by_phone/:phone_number/time_slots', :to => 'experts#get_by_phone', :as => :string

  get '/experts/:expert_id/:day_number/getPossibleTimeSlots', :to => 'experts#get_possible_time_slots'
  get '/experts/:expert_id/workingDays', :to => 'experts#get_working_days'
  resources :procedures do
    resources :experts
  end

  resources :experts do
    resources :expProcedures, controller: 'exp_procedures'
    resources :procedures, controller: 'procedures'
    resources :time_slots, controller: 'time_slots'
  end

  controller :time_slots do
    get 'time_slots/get_by_client_phone/:phone_number', :to => 'time_slots#get_by_client_phone'
    get 'time_slots', :to => "time_slots#index"
    get 'time_slots/:date', :to => "time_slots#index", :as => :date
    post 'time_slots', :to => "time_slots#create"
  end
end
