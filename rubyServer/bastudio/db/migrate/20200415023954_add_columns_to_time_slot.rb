class AddColumnsToTimeSlot < ActiveRecord::Migration[6.0]
  def change
    add_column :time_slots, :startDateTime, :dateTime
    add_column :time_slots, :endDateTime, :dateTime
    add_column :time_slots, :price, :integer
  end
end
