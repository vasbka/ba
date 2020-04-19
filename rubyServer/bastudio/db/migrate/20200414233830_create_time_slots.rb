class CreateTimeSlots < ActiveRecord::Migration[6.0]
  def change
    create_table :time_slots do |t|
      t.references :expert, null: false, foreign_key: true
      t.references :exp_procedure, null: false, foreign_key: true
      t.startDateTime :datetime
      t.endDateTime :datetime
      t.integer :price

      t.timestamps
    end
  end

end
