class CreateCategories < ActiveRecord::Migration[6.0]
  def change
    create_table :categories do |t|
      t.string :name
      t.integer :category_type, default: 0
      t.integer :sub_type, default: 0

      t.timestamps
    end
    add_index :categories, :name
    add_index :categories, :category_type
    add_index :categories, :sub_type
  end
end
