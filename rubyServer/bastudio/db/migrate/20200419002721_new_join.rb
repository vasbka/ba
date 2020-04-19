class NewJoin < ActiveRecord::Migration[6.0]
  def change
    create_table :time_slots_exp_procedures, id: false do |t|
      t.belongs_to :time_slot
      t.belongs_to :exp_procedure
    end
  end
end
