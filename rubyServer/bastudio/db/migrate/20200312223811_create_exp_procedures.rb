class CreateExpProcedures < ActiveRecord::Migration[6.0]
  def change
    create_table :exp_procedures do |t|
      t.integer :price
      t.references :expert, null: false, foreign_key: true
      t.references :procedure, null: false, foreign_key: true

      t.timestamps
    end
  end
end
