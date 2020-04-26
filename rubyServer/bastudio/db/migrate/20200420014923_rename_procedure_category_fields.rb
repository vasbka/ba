class RenameProcedureCategoryFields < ActiveRecord::Migration[6.0]
  def change
    rename_column :procedure_categories, :category_name, :name
    add_column :procedure_categories, :description, :text
  end
end
