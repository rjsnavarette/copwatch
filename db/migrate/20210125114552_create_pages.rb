class CreatePages < ActiveRecord::Migration[6.0]
  def change
    create_table :pages do |t|
      t.string :title
      t.string :content
      t.integer :category_id
      t.integer :page_type

      t.timestamps
    end
    add_index :pages, :title
    add_index :pages, :category_id
    add_index :pages, :page_type
  end
end
