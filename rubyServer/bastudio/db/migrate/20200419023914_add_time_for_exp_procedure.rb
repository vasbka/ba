class AddTimeForExpProcedure < ActiveRecord::Migration[6.0]
  def change
    add_column :exp_procedures, :time_consume, :integer
  end
end
