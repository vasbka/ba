class CreateExperts < ActiveRecord::Migration[6.0]
  def change
    create_table :experts do |t|
      t.string :firstName
      t.string :lastName
      t.string :secondName
      t.string :description
      t.string :phoneNumber

      t.timestamps
    end
  end
end
