class AddAdditionalToTimeSlotAndAddReferenceToClient < ActiveRecord::Migration[6.0]
  def change
    add_column :clients, :instagram, :string
    add_column :time_slots, :additional, :string
    add_reference :time_slots, :client, foreign_key: true
    create_table :procedure_categories do |t|
      t.string :category_name
    end
    create_table :procedure_categories_procedures, id: false do |t|
      t.belongs_to :procedure
      t.belongs_to :procedure_category
    end
  end
end
