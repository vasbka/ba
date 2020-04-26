class AddClientColumns < ActiveRecord::Migration[6.0]
  def change
    add_column :time_slots, :client_first_name, :string    
    add_column :time_slots, :client_phone_number, :string
  end
end
