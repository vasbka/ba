class RenameJoinTable < ActiveRecord::Migration[6.0]
  def change
    drop_table :time_slots_exp_procedures
    create_table :exp_procedures_time_slots, id: false do |t|
      t.belongs_to :time_slot
      t.belongs_to :exp_procedure
    end
  end
end
