class CreateExpertDays < ActiveRecord::Migration[6.0]
  def change
    create_table :expert_days do |t|
      t.integer :dayNumber
      t.time :startTime
      t.time :endTime
      t.references :expert, null: false, foreign_key: true

      t.timestamps
    end
  end
end
