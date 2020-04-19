class Fix < ActiveRecord::Migration[6.0]
  def change    
    drop_table :time_slots_expert_procedures
    drop_table :tests
  end
end
