class Procedure < ApplicationRecord
  has_many :exp_procedures
  has_many :experts, through: :exp_procedures
  has_and_belongs_to_many :procedure_category
  validates :title, presence: true, uniqueness: true
  validates :description, presence: true
  has_many :time_slots, :through => :exp_procedures

  def as_json(options={})
    json_to_return = super
      if options.has_key? :expertId
        price = ExpProcedure.where(expert_id: options[:expertId], procedure_id: self.id)[0].price
        json_to_return[:price] = price
      end
    return json_to_return
  end
end
